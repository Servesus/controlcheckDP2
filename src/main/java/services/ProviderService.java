package services;

import domain.Actor;
import domain.Curricula;
import domain.MiscData;
import domain.Rookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.MiscDataRepository;
import repositories.ProviderRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Collection;

@Service
@Transactional
public class ProviderService {

    //Managed Repository
    @Autowired
    private ProviderRepository providerRepository;

}
