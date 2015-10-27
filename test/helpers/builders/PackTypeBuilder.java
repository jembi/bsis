package helpers.builders;

import model.packtype.PackType;

public class PackTypeBuilder extends AbstractEntityBuilder<PackType> {
    
    private Integer id;
    private Boolean testSampleProduced;

    public PackTypeBuilder withId(Integer id) {
        this.id = id;
        return this;
    }
    
    public PackTypeBuilder withTestSampleProduced(boolean testSampleProduced) {
        this.testSampleProduced = testSampleProduced;
        return this;
    }

    @Override
    public PackType build() {
        PackType packType = new PackType();
        packType.setId(id);
        if (testSampleProduced != null) {
            packType.setTestSampleProduced(testSampleProduced);
        }
        return packType;
    }
    
    public static PackTypeBuilder aPackType() {
        return new PackTypeBuilder();
    }

}
