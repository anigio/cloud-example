package com.anigio.example.cloudapi.domain;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by Eric.Shang on 2019-08-09.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFuncStateRequest implements Serializable {

    private String deviceId;
    private List<String> funcIdentity;

    public List<String> getFuncIdentity() {
        return Optional.ofNullable(funcIdentity).orElse(Lists.newArrayList());
    }
}
