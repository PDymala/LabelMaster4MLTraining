package weka;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.Random;

import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.*;


/**
 * Following application is a part of Label Master 4 package. It's purpose is to train machine learning model based on a database created by crypto module. 
 * It's a separate application because it has to use old version of WEKA 3.4.1. That is the only version available on Android and both, model and verification app has to have the same version.
 * While crypto uses newest one for convenience purpose.
 *
 * @author Piotr Dymala p.dymala@gmail.com
 * @version 1.0
 * @since 2021-05-06
 */



public class ModelTraining {

	public static void main(String[] args) {

		try {
			
			//name of data base created by Crypto module
			String nameOfTrainingSetFile = "Trainingset_hash_50000_9_32mixOfHashes";
			
			System.out.println("Weka Tool");
			BufferedReader breader = new BufferedReader(new FileReader(nameOfTrainingSetFile+".arff"));
			Instances train = new Instances(breader);
			train.setClassIndex(train.numAttributes() - 1);
			breader.close(); // loading training data

//		        BufferedReader treader = new BufferedReader(new FileReader("Training1000000000_AES_30k_fullcode_badpass3.arff"));
//		        Instances test = new Instances(treader);
//		        test.setClassIndex(test.numAttributes() -1);
//		        treader.close();        //loading testing data

			Classifier cls = new J48();
			cls.buildClassifier(train);

			Evaluation eval = new Evaluation(train);

//				if you have a test db, use this.
//		        eval.evaluateModel(cls, test);  //
//		        System.out.println(eval.toSummaryString("\nResults\n======\n", false));

//				if not, use crossvalidation
			eval.crossValidateModel(cls, train, 10, new Random(1));
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));

			
			
			//save the ML model for old versions of WEKA
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
					nameOfTrainingSetFile+".model"));
			oos.writeObject(cls);
			oos.flush();
			oos.close();
			
			
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println("dupa");

		}

	}

}
