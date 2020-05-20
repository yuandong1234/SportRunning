package com.yuong.running.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于记录一条轨迹，包括起点、终点、轨迹中间点、距离、耗时、时间
 */
public class PathRecord implements Parcelable {
    //主键
    private Long id;
    //运动开始点
    private LatLng mStartPoint;
    //运动结束点
    private LatLng mEndPoint;
    //运动轨迹
    private List<LatLng> mPathLinePoints = new ArrayList<>();
    //运动距离
    private Double mDistance;
    //运动时长
    private Long mDuration;
    //运动开始时间
    private Long mStartTime;
    //运动结束时间
    private Long mEndTime;
    //消耗卡路里
    private Double mCalorie;
    //平均时速(公里/小时)
    private Double mSpeed;
    //日期标记
    private String mDateTag;

    public PathRecord() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LatLng getStartPoint() {
        return mStartPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.mStartPoint = startPoint;
    }

    public LatLng getEndPoint() {
        return mEndPoint;
    }

    public void setEndPoint(LatLng endPoint) {
        this.mEndPoint = endPoint;
    }

    public List<LatLng> getPathLinePoints() {
        return mPathLinePoints;
    }

    public void setPathLinePoints(List<LatLng> pathLinePoints) {
        this.mPathLinePoints = pathLinePoints;
    }

    public Double getDistance() {
        return mDistance;
    }

    public void setDistance(Double distance) {
        this.mDistance = distance;
    }

    public Long getDuration() {
        return mDuration;
    }

    public void setDuration(Long mDuration) {
        this.mDuration = mDuration;
    }

    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Long startTime) {
        this.mStartTime = startTime;
    }

    public Long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Long endTime) {
        this.mEndTime = endTime;
    }

    public Double getCalorie() {
        return mCalorie;
    }

    public void setCalorie(Double calorie) {
        this.mCalorie = calorie;
    }

    public Double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Double speed) {
        this.mSpeed = speed;
    }

    public String getDateTag() {
        return mDateTag;
    }

    public void setDateTag(String dateTag) {
        this.mDateTag = dateTag;
    }

    public void addpoint(LatLng point) {
        mPathLinePoints.add(point);
    }

    protected PathRecord(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        mStartPoint = in.readParcelable(LatLng.class.getClassLoader());
        mEndPoint = in.readParcelable(LatLng.class.getClassLoader());
        mPathLinePoints = in.createTypedArrayList(LatLng.CREATOR);
        if (in.readByte() == 0) {
            mDistance = null;
        } else {
            mDistance = in.readDouble();
        }
        if (in.readByte() == 0) {
            mDuration = null;
        } else {
            mDuration = in.readLong();
        }
        if (in.readByte() == 0) {
            mStartTime = null;
        } else {
            mStartTime = in.readLong();
        }
        if (in.readByte() == 0) {
            mEndTime = null;
        } else {
            mEndTime = in.readLong();
        }
        if (in.readByte() == 0) {
            mCalorie = null;
        } else {
            mCalorie = in.readDouble();
        }
        if (in.readByte() == 0) {
            mSpeed = null;
        } else {
            mSpeed = in.readDouble();
        }
        mDateTag = in.readString();
    }

    public static final Creator<PathRecord> CREATOR = new Creator<PathRecord>() {
        @Override
        public PathRecord createFromParcel(Parcel in) {
            return new PathRecord(in);
        }

        @Override
        public PathRecord[] newArray(int size) {
            return new PathRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeParcelable(mStartPoint, flags);
        dest.writeParcelable(mEndPoint, flags);
        dest.writeTypedList(mPathLinePoints);
        if (mDistance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mDistance);
        }
        if (mDuration == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mDuration);
        }
        if (mStartTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mStartTime);
        }
        if (mEndTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mEndTime);
        }
        if (mCalorie == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mCalorie);
        }
        if (mSpeed == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mSpeed);
        }
        dest.writeString(mDateTag);
    }
}
