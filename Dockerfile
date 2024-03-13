FROM --platform=linux/x86_64 openjdk:17

WORKDIR /ecommerce

COPY ./build/libs/ecommerce-1.0.0.jar ecommerce.jar

EXPOSE 9092

ENTRYPOINT ["java", "-jar", "ecommerce.jar"]
