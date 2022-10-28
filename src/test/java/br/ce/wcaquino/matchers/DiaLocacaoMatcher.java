package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

public class DiaLocacaoMatcher extends TypeSafeMatcher<Date> {

    private Integer dias;

    public DiaLocacaoMatcher(Integer dias) {
        this.dias = dias;
    }

    //@Override
    public void describeTo(Description desc) {
        Date data = DataUtils.obterDataComDiferencaDias(dias);
        desc.appendText(String.valueOf(data));
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(dias));
    }
}
