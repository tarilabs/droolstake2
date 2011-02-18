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

package org.drools.examples.manners;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Guest
    implements
    Externalizable {
    /**
     *
     */
    private static final long serialVersionUID = 510l;

    private String            name;

    private Sex               sex;

    private Hobby             hobby;

    public Guest() {
    }

    public Guest(final String name,
                 final Sex sex,
                 final Hobby hobby) {
        this.name = name;
        this.sex = sex;
        this.hobby = hobby;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name    = (String)in.readObject();
        sex     = (Sex)in.readObject();
        hobby   = (Hobby)in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(sex);
        out.writeObject(hobby);
    }

    public String getName() {
        return this.name;
    }

    public Hobby getHobby() {
        return this.hobby;
    }

    public Sex getSex() {
        return this.sex;
    }

    public String toString() {
        return "[Guest name=" + this.name + ", sex=" + this.sex + ", hobbies=" + this.hobby + "]";
    }
}
