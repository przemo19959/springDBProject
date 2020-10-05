package pl.dabrowski.GameShop.entities.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ShortcutConverter implements AttributeConverter<String, String> {
	@Override
	public String convertToDatabaseColumn(String attribute) {
		return attribute.toUpperCase();
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		return dbData;
	}
}
