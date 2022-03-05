
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import java.lang.Exception;
import java.lang.reflect.InaccessibleObjectException;

public class TestDataLoading {
    public static void main(String[] args) throws Exception {
		// loads data and set class index
        try {
            String dataSource = Utils.getOption("t", args);
            Instances data = DataSource.read(dataSource);
            String clsIndex = Utils.getOption("c", args);
        } catch (InaccessibleObjectException e) {
            System.out.println("");
        }
    }
}
