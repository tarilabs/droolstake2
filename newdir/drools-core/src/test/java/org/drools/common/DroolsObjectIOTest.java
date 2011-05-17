/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import org.drools.core.util.DroolsStreamUtils;
import org.drools.rule.GroupElement;
import org.drools.rule.Package;
import org.junit.Ignore;
import org.junit.Test;

public class DroolsObjectIOTest {

    private static class FooBar implements Serializable {

        private String value = "hello";

        public FooBar() {
        }
    }

    @Test
    public void testFileIO() throws Exception {
        FooBar fooBar1 = new FooBar();
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOut).writeObject(fooBar1);
        ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(byteArrayOut.toByteArray());
        FooBar fooBar2 = (FooBar) new ObjectInputStream(byteArrayIn).readObject();

        final String TEST_FILE = "test.dat";
        File file = new File(getClass().getResource("DroolsObjectIOTest.class").getFile());
        File parentFile = new File(file.getParent().replaceAll("%20", " "), TEST_FILE);
        GroupElement testGroupElement = new GroupElement();
        DroolsStreamUtils.streamOut(new FileOutputStream(parentFile), testGroupElement);

        InputStream fis = getClass().getResourceAsStream(TEST_FILE);
        GroupElement streamedGroupElement = (GroupElement) DroolsStreamUtils.streamIn(fis);

        assertEquals(streamedGroupElement, testGroupElement);
    }

    public static class SerializableObject implements Serializable {
        protected int value = 123;
        protected String name;

        public SerializableObject() {
            this("SerializableObject");
        }
        public SerializableObject(String name) {
            this.name   = name;
        }

        // TODO bug: breaks equals - hashcode contract
        public boolean equals(Object obj) {
            if (obj instanceof SerializableObject) {
                return value == ((SerializableObject)obj).value;
            }
            return false;
        }
        public String toString() {
            return new StringBuilder(name).append('|').append(value).toString();
        }
    }

    public static class ExternalizableObject extends SerializableObject implements Externalizable {

        public ExternalizableObject() {
            super("ExternalizableObject");
        }
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            value   = in.readInt();
            name    = (String)in.readObject();
        }
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(value);
            out.writeObject(name);
        }
    }

    @Test
    public void testObject() throws Exception {
        SerializableObject    obj = new ExternalizableObject();

        byte[]  buf = serialize(obj);
        assertEquals(deserialize(buf), obj);

        obj = new SerializableObject();
        buf = serialize(obj);
        assertEquals(deserialize(buf), obj);
    }

    private static Object deserialize(byte[] buf) throws Exception {
        return new DroolsObjectInputStream(new ByteArrayInputStream(buf)).readObject();
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream   bytes   = new ByteArrayOutputStream();
        ObjectOutput            out = new DroolsObjectOutputStream(bytes);

        out.writeObject(obj);
        out.flush();
        out.close();

        return bytes.toByteArray();
    }

    private static Object unmarshal(byte[] buf) throws Exception {
        return new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
    }

    private static byte[] marshal(Object obj) throws IOException {
        ByteArrayOutputStream   bytes   = new ByteArrayOutputStream();
        ObjectOutput            out = new ObjectOutputStream(bytes);

        out.writeObject(obj);
        out.flush();
        out.close();

        return bytes.toByteArray();
    }

    @Test
    public void testStreaming() throws Exception {
        Package pkg = new Package("test");

        byte[]  buf = marshal(pkg);
        assertEquals(unmarshal(buf), pkg);

        buf = serialize(pkg);
        assertEquals(deserialize(buf), pkg);
    }
}
