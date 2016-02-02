# Thrums
======

## Thrums - Validation
Validate JSON instances against JSON schema (draft4)

### Usge

#### Setup

    Keyword[] keywords = new Keyword[]{
        new AllOf(),
        new AnyOf(),
        new no.thrums.validation.engine.keyword.any.Enum(),
        new Not(),
        new OneOf(),
        new Type(),
        new AdditionalItems(),
        new MaxItems(),
        new MinItems(),
        new UniqueItems(),
        new Maximum(),
        new Minimum(),
        new MultipleOf(),
        new AdditionalProperties(),
        new Dependencies(),
        new MaxProperties(),
        new MinProperties(),
        new Required(),
        new MaxLength(),
        new MinLength(),
        new Pattern(),
        new DateTime(),
        new Email(),
        new Hostname(),
        new Ipv4(),
        new Ipv6(),
        new Uri()
    };
    Mapper mapper = new JacksonMapper(new ObjectMapper());
    InstanceLoader instanceLoader = new EngineInstanceLoader(mapper);
    InstanceFactory instanceFactory = new EngineInstanceFactory(instanceLoader);
    PathFactory pathFactory = new EnginePathFactory();
    Validator validator = new EngineValidator(instanceFactory, pathFactory, keywords);

#### Instance creation

    Map<String, Object> map = new LinkedHashMap<>();
    map.put("one", "1");
    map.put("two", 2);
    map.put("bool", false);
    
    List<Object> list = new LinkedList<>();
    list.add("1");
    list.add(2);
    list.add(false);
    
    java.lang.Object object = new Object() {
        private String one = "1";
        private Integer two = 2;
        private boolean bool = false;
    
        public String getOne() {return one;}
        public void setOne(String one) {this.one = one;}
        public Integer getTwo() {return two;}
        public void setTwo(Integer two) {this.two = two;}
        public boolean isThree() {return bool;}
        public void setThree(boolean bool) {this.bool = bool;}
    };
    
    String string = "{\"one\":\"1\",\"two\":2,\"bool\":false}";
    
    Instance instanceFromClasspath = instanceLoader.loadUri(instanceFactory, URI.create("classpath:/instance.json"));
    
    Instance instanceFromfile = instanceLoader.loadUri(instanceFactory, URI.create("file:/instance.json"));
    
    Instance instanceFromUrl = instanceLoader.loadUri(instanceFactory, URI.create("http://localhost/instance.json"));
    
    Instance instanceFromReader = null;
    try (StringReader stringReader = new StringReader(string)) {
        instanceFromReader = instanceLoader.loadReader(instanceFactory, stringReader);
    }
    
    Instance instanceFromMap = instanceFactory.defined(map);
    
    Instance instanceFromList = instanceFactory.defined(list);
    
    Instance instanceFromObject = instanceFactory.defined(object);
    
    Instance instanceFromString = instanceFactory.defined(mapper.read(string, java.lang.Object.class));

#### Validation

    Instance schema = instanceFactory.defined(mapper.read(
        "{" +
        "  \"$schema\": \"http://json-schema.org/draft-04/schema#\"," +
        "  \"additionalProperties\":false," +
        "  \"required\":[\"bool\"]," +
        "  \"properties\": {" +
        "    \"one\":{" +
        "      \"type\":\"string\"" +
        "    }," +
        "    \"two\":{" +
        "      \"type\":\"number\"" +
        "    }," +
        "    \"bool\":{" +
        "      \"type\":\"boolean\"" +
        "    }" +
        "  }" +
        "}",
        Object.class)
    );

    List<Violation> violations = validator.validate(schema, instanceFromObject);
    if (!violations.isEmpty()) {
        for (Violation violation : violations) {
            System.out.println("Path: " + violation.getPath());
            System.out.println("Message: " + violation.getMessage(Locale.forLanguageTag("en")));
        }
    }
