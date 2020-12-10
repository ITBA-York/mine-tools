package cn.tripman.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DbProperties {

    private String userName;

    private String url;

    private String password;

    private DriverEnum driverEnum;

    public enum DriverEnum {

        MYSQL("com.mysql.jdbc.Driver"),

        POSTGRESQL("org.postgresql.Driver");

        private String value;

        DriverEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
