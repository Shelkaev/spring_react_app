package ru.bank.application.service.rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.application.api.data.rate.RateApiRequest;
import ru.bank.application.api.data.rate.RateIdApiRequest;
import ru.bank.application.dto.rate.RateDto;
import ru.bank.application.dto.rate.RateFactoryDto;
import ru.bank.application.exception.deposit.RateNotFoundException;
import ru.bank.persistence.entity.deposit.Rate;
import ru.bank.persistence.entity.deposit.RateState;
import ru.bank.persistence.repository.deposit.RateRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RateService{
    @Autowired
    private RateRepository rateRepository;

    public void createRate(RateApiRequest request) {
        Rate rate = new Rate();
        rate.setName(request.getName());
        rate.setMaxAmount(request.getMaxAmount());
        rate.setMinAmount(request.getMinAmount());
        rate.setPercent(request.getPercent());
        rate.setNumberOfMonth(request.getNumberOfMonth());
        rate.setState(RateState.ACTIVE);
        rate.setHasEarlyClosed(request.isHasEarlyClosed());
        rate.setHasIncreased(request.isHasIncreased());
        rate.setHasCapitalized(request.isHasCapitalized());
        rate.setHasWithdrawal(request.isHasWithdrawal());
        rateRepository.save(rate);
    }

    public List<RateDto> getRates() {
        List<Rate> rates = rateRepository.findAll()
                .stream()
                .filter(rate -> rate.getState() == RateState.ACTIVE)
                .collect(Collectors.toList());
        if (rates.isEmpty()){
            throw new RateNotFoundException();
        }
        return RateFactoryDto.createRateList(rates);
    }
    public RateDto getOneRate(long id) {
        Rate rate = rateRepository.findById(id).orElseThrow(RateNotFoundException::new);
        return RateFactoryDto.createRate(rate);
    }

    public void changeRate(RateApiRequest request) {
        Rate rate = rateRepository.findById(request.getId())
                .orElseThrow(RateNotFoundException::new);
        rate.setName(request.getName());
        rate.setMaxAmount(request.getMaxAmount());
        rate.setMinAmount(request.getMinAmount());
        rate.setPercent(request.getPercent());
        rate.setNumberOfMonth(request.getNumberOfMonth());
        rate.setHasEarlyClosed(request.isHasEarlyClosed());
        rate.setHasIncreased(request.isHasIncreased());
        rate.setHasCapitalized(request.isHasCapitalized());
        rate.setHasWithdrawal(request.isHasWithdrawal());
        rateRepository.save(rate);
    }

    public void closeRate(RateIdApiRequest request) {
        Rate rate = rateRepository.findById(request.getId())
                .orElseThrow(RateNotFoundException::new);
        rate.setState(RateState.ARCHIVE);
        rateRepository.save(rate);
    }


}
