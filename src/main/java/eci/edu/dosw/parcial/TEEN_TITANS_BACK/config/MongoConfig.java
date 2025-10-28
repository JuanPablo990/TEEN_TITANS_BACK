package eci.edu.dosw.parcial.TEEN_TITANS_BACK.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "teen_titans";
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder.applyConnectionString(
                        new ConnectionString("mongodb+srv://nietocortesjuanpablo_db_user:eyzko982M7ZRBwIA@cluster0.qgsoyo2.mongodb.net/teen_titans?retryWrites=true&w=majority")
                )
                .applyToSslSettings(ssl -> {
                    ssl.enabled(true);
                    ssl.invalidHostNameAllowed(true);
                });
    }
}