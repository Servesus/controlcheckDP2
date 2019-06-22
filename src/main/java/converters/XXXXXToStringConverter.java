
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.XXXXX;

@Component
@Transactional
public class XXXXXToStringConverter implements Converter<XXXXX, String> {

	@Override
	public String convert(final XXXXX w) {
		String result;

		if (w == null)
			result = null;
		else
			result = String.valueOf(w.getId());

		return result;
	}

}
