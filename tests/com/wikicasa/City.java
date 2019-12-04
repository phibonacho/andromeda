package com.wikicasa;

import com.annotation.validate.Validate;
import com.annotation.validate.types.LPositive;

public class City {
    private long istatCode;

    @Validate(with = LPositive.class, mandatory = true)
    public long getIstatCode() {
        return istatCode;
    }

    public void setIstatCode(long istatCode) {
        this.istatCode = istatCode;
    }
}
