package victor.training.oo.behavioral.strategy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class StrategySpringApp implements CommandLineRunner {
	public static void main(String[] args) {
		new SpringApplicationBuilder(StrategySpringApp.class)
			.profiles("localProps")
			.run(args);
	}

	
	private ConfigProvider configProvider = new ConfigFileProvider(); 
	
	// TODO [1] Break CustomsService logic into Strategies
	// TODO [2] Convert it to Chain Of Responsibility
	// TODO [3] Wire with Spring
	// TODO [4] ConfigProvider: selected based on environment props, with Spring
	public void run(String... args) throws Exception {
		CustomsService service = new CustomsService();
		System.out.println("Tax for (RO,100,100) = " + service.computeCustomsTax("RO", 100, 100));
		System.out.println("Tax for (CN,100,100) = " + service.computeCustomsTax("CN", 100, 100));
		System.out.println("Tax for (UK,100,100) = " + service.computeCustomsTax("UK", 100, 100));
		
		System.out.println("Property: " + configProvider.getProperties().getProperty("someProp"));
	}
}

class CustomsService {
	public double computeCustomsTax(String originCountry, double tobacoValue, double regularValue) { // UGLY API we CANNOT change
		TaxCalculator ceva = selectTaxCalculator(originCountry); 
		return ceva.calculate(tobacoValue, regularValue);
	}

	private TaxCalculator selectTaxCalculator(String originCountry) {
		switch (originCountry) { 
		case "UK": return new UKTaxCalculator();
		case "CN": return new CNTaxCalculator();
		case "FR": 
		case "ES": // other EU country codes...
		case "RO": return new EUTaxCalculator();
		default: throw new IllegalArgumentException(originCountry);
		}
	}
}

class UKTaxCalculator  implements TaxCalculator{
	public double calculate(double tobacoValue, double regularValue) {
		return tobacoValue/2 + regularValue/2;
	}
}

class CNTaxCalculator  implements TaxCalculator{
	public double calculate(double tobacoValue, double regularValue) {
		return tobacoValue + regularValue;
	}
}







interface TaxCalculator {
	double calculate(double tobacoValue, double regularValue);
		
}
class EUTaxCalculator implements TaxCalculator{
	public double calculate(double tobacoValue, double regularValue) {
		return tobacoValue/3;
	}
}

