package helpers.builders;

import model.packtype.PackType;

public class PackTypeBuilder extends AbstractEntityBuilder<PackType> {
    
    private Integer id;

    public PackTypeBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public PackType build() {
        PackType packType = new PackType();
        packType.setId(id);
        return packType;
    }
    
    public static PackTypeBuilder aPackType() {
        return new PackTypeBuilder();
    }

}
