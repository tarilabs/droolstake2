package org.drools.persistence.info;

import org.drools.persistence.SessionMarshallingHelper;

import javax.persistence.*;
import java.util.Date;

@Entity
@SequenceGenerator(name = "sessionInfoIdSeq", sequenceName = "SESSIONINFO_ID_SEQ")
public class SessionInfo {

    private
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sessionInfoIdSeq")
    Integer id;

    @Version
    @Column(name = "OPTLOCK")
    private int version;

    private Date startDate;
    private Date lastModificationDate;

    @Lob
    @Column(length = 2147483647)
    private byte[] rulesByteArray;

    @Transient
    SessionMarshallingHelper helper;

    public SessionInfo() {
        this.startDate = new Date();
    }

    public Integer getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setJPASessionMashallingHelper(SessionMarshallingHelper helper) {
        this.helper = helper;
    }

    public SessionMarshallingHelper getJPASessionMashallingHelper() {
        return helper;
    }

    public void setData(byte[] data) {
        this.rulesByteArray = data;
    }

    public byte[] getData() {
        return this.rulesByteArray;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getLastModificationDate() {
        return this.lastModificationDate;
    }

    public void setLastModificationDate(Date date) {
        this.lastModificationDate = date;
    }


    @PrePersist
    @PreUpdate
    public void update() {
        this.rulesByteArray = this.helper.getSnapshot();
    }

    public void setId(Integer ksessionId) {
        this.id = ksessionId;
    }

}
