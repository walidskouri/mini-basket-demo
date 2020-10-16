
java -Dservice.name=STOCK-MS -Dapplication.name=STOCK-MS -Dotel.exporter=jaeger -Dotel.jaeger.endpoint=localhost:14250 -Dotel.jaeger.service.name=STOCK-MS -javaagent:agent/opentelemetry-javaagent-0.9.0-all.jar -jar stock/target/stock-0.0.1-SNAPSHOT.jar
java -Dservice.name=BASKET-MS -Dapplication.name=BASKET-MS -Dotel.exporter=jaeger -Dotel.jaeger.endpoint=localhost:14250 -Dotel.jaeger.service.name=BASKET-MS -javaagent:agent/opentelemetry-javaagent-0.9.0-all.jar -jar basket/target/basket-0.0.1-SNAPSHOT.jar
java -Dservice.name=PRODUCT-MS -Dapplication.name=PRODUCT-MS -Dotel.exporter=jaeger -Dotel.jaeger.endpoint=localhost:14250 -Dotel.jaeger.service.name=PRODUCT-MS -javaagent:agent/opentelemetry-javaagent-0.9.0-all.jar -jar products/target/products-0.0.1-SNAPSHOT.jar



java -Dapplication.name=STOCK-MS -Dotel.exporter=jaeger -Dotel.jaeger.endpoint=localhost:14250 -Dotel.jaeger.service.name=STOCK-MS -javaagent:agent/opentelemetry-javaagent-all.jar -jar stock/target/stock-0.0.1-SNAPSHOT.jar
java -Dapplication.name=BASKET-MS -Dotel.exporter=jaeger -Dotel.jaeger.endpoint=localhost:14250 -Dotel.jaeger.service.name=BASKET-MS -javaagent:agent/opentelemetry-javaagent-all.jar -jar basket/target/basket-0.0.1-SNAPSHOT.jar
java -Dapplication.name=PRODUCT-MS -Dotel.exporter=jaeger -Dotel.jaeger.endpoint=localhost:14250 -Dotel.jaeger.service.name=PRODUCT-MS -javaagent:agent/opentelemetry-javaagent-all.jar -jar products/target/products-0.0.1-SNAPSHOT.jar


