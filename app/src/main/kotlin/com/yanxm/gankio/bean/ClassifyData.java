package com.yanxm.gankio.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * Created by dell on 2017/8/24.
 */

@Entity
public class ClassifyData implements Parcelable {
    /**
     * _id : 59957ec7421aa9672f354ddd
     * createdAt : 2017-08-17T19:32:23.785Z
     * desc : 让你明明白白的使用RecyclerView——SnapHelper详解
     * images : ["http://img.gank.io/55147d8b-513d-4bbc-ad67-ba20a3ec297c"]
     * publishedAt : 2017-08-23T12:12:15.166Z
     * source : web
     * type : Android
     * url : http://www.jianshu.com/p/e54db232df62
     * used : true
     * who :
     */
    @Id(autoincrement = true)
    private Long id;
    @Transient
    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Transient
    private List<String> images;
    @Generated(hash = 1321564048)
    public ClassifyData(Long id, String createdAt, String desc, String publishedAt, String source, String type,
            String url, boolean used, String who) {
        this.id = id;
        this.createdAt = createdAt;
        this.desc = desc;
        this.publishedAt = publishedAt;
        this.source = source;
        this.type = type;
        this.url = url;
        this.used = used;
        this.who = who;
    }

    @Generated(hash = 1338862470)
    public ClassifyData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String get_id() {
        return this._id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getPublishedAt() {
        return this.publishedAt;
    }
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
    public String getSource() {
        return this.source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean getUsed() {
        return this.used;
    }
    public void setUsed(boolean used) {
        this.used = used;
    }
    public String getWho() {
        return this.who;
    }
    public void setWho(String who) {
        this.who = who;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this._id);
        dest.writeString(this.createdAt);
        dest.writeString(this.desc);
        dest.writeString(this.publishedAt);
        dest.writeString(this.source);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeByte(this.used ? (byte) 1 : (byte) 0);
        dest.writeString(this.who);
        dest.writeStringList(this.images);
    }

    protected ClassifyData(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this._id = in.readString();
        this.createdAt = in.readString();
        this.desc = in.readString();
        this.publishedAt = in.readString();
        this.source = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.used = in.readByte() != 0;
        this.who = in.readString();
        this.images = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ClassifyData> CREATOR = new Parcelable.Creator<ClassifyData>() {
        @Override
        public ClassifyData createFromParcel(Parcel source) {
            return new ClassifyData(source);
        }

        @Override
        public ClassifyData[] newArray(int size) {
            return new ClassifyData[size];
        }
    };
}
