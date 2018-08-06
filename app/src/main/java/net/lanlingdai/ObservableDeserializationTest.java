//package net.lanlingdai;
//
//
//import android.databinding.ObservableField;
//
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonParser;
//
//import junit.framework.Assert;
//
//import java.io.IOException;
//
//public class ObservableDeserializationTest {
//
//    private static class ObservableDeserializer extends JsonDeserializer<ObservableField> implements ContextualDeserializer {
//
//        private Class<?> mTargetClass;
//
//        @Override
//        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
//            mTargetClass = property.getType().containedType(0).getRawClass();
//            return this;
//        }
//
//        @Override
//        public ObservableField deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//            ObservableField result = new ObservableField();
//            result.set(p.readValueAs(mTargetClass));
//            return result;
//        }
//
//
//    }
//
//    private static class SomePojo {
//        public String id;
//        public String name;
//    }
//
//    private static class ObservableTestClass {
//
//        @JsonDeserialize(using = ObservableDeserializer.class)
//        public ObservableField<SomePojo> testObj = new ObservableField<>();
//
//    }
//
//    @Test
//    public void DeserializingAnObservableObjectShouldSetValueCorrectly() {
//        ObservableTestClass tc = null;
//        try {
//            tc = new ObjectMapper().readValue("{\"testObj\":{\"name\":\"TestName\",\"id\":\"TestId\"}}", ObservableTestClass.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Assert.assertEquals("TestName", tc.testObj.get().name);
//        Assert.assertEquals("TestId", tc.testObj.get().id);
//    }
//
//
//}
