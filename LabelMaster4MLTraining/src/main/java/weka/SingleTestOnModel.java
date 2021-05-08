package weka;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.*;
/**
 * Application to test a single instance on machine learning model created with ModelTraining.class
 *
 * @author Piotr Dymala p.dymala@gmail.com
 * @version 1.0
 * @since 2021-05-06
 */

public class SingleTestOnModel {

	public static void main(String[] args) {

		try {
			int standardStringLenght = 9; // standard number
			int secureStringLenght = 32;

			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream("Trainingset_hash_50000_9_32mixOfHashes.model"));
			Classifier cls = (Classifier) ois.readObject();
			ois.close();

			System.out.println("Weka Tool");

			// for new weka
			// https://stackoverflow.com/questions/29460216/i-would-like-to-classify-new-instance-but-i-dont-want-to-load-the-dataset-to-cl
			// this is very old implementation, required by 3.4.3

			// Create vector to hold string values
			// for some reason , it always give the first value YES, even if not specified
			// in FAST VECTOR
			FastVector my_nominal_values = new FastVector(2);
			my_nominal_values.addElement("YES");

			my_nominal_values.addElement("NO");

			// Create nominal attribute
			Attribute isCorrect = new Attribute("IsCorrect", my_nominal_values);

			FastVector attss = new FastVector();
			for (int y = 0; y < standardStringLenght; y++) {

				attss.addElement(new Attribute("Standard_string" + y));

			}
			// secure number
			for (int y = 0; y < secureStringLenght; y++) {

				attss.addElement(new Attribute("Secure_string" + y));
			}

			attss.addElement(isCorrect);

			Instances datatest = new Instances("Value_to_be_checked", attss, 0);

			double[] instanceValue1 = new double[datatest.numAttributes()];

			// tested value

			double[] value = { 1, 4, 9, 7, 2, 1, 9, 7, 5, 107, 66, 114, 105, 78, 106, 112, 64, 53, 33, 90, 107, 87, 55,
					116, 112, 68, 108, 90, 35, 119, 76, 85, 115, 112, 87, 112, 50, 107, 78, 68, 83, 1 };
			// 0 as YES, 1 as NO - those are labels from Instance
			// 1,4,9,7,2,1,9,7,5,107,66,114,105,78,106,112,64,53,33,90,107,87,55,116,112,68,108,90,35,119,76,85,115,112,87,112,50,107,78,68,83,NO

			for (int u = 0; u < value.length; u++) {
				double temp = value[u];
				instanceValue1[u] = temp;

			}

			datatest.add(new Instance(1.0, instanceValue1));

			// without this, it shows exception
			// https://stackoverflow.com/questions/40318420/weka-core-unassignedclassexception-class-index-is-negative-not-set

			if (datatest.classIndex() == -1) {
				System.out.println("reset index...");
				datatest.setClassIndex(datatest.numAttributes() - 1);
			}

			// PREDICTING !!!!
			double prediction = cls.classifyInstance(datatest.firstInstance());

			System.out.println("prediction as double: " + prediction);
			System.out.println("prediction as name: " + datatest.classAttribute().value((int) prediction));

//	

		} catch (Exception e) {

			e.printStackTrace();
			System.out.println("dupa");

		}

	}

}
