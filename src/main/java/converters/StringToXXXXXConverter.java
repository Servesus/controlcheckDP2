
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.XXXXXRepository;
import domain.XXXXX;

@Component
@Transactional
public class StringToXXXXXConverter implements Converter<String, XXXXX> {

	@Autowired
	XXXXXRepository	repository;


	@Override
	public XXXXX convert(final String text) {
		XXXXX result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.repository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
