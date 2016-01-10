package nz.co.senanque.madura.bundle.spring;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;
import org.springframework.util.MultiValueMap;

public class BeanUtils {

	private BeanUtils() {
		
	}
	
	/**
	 * This is different from the usual bean factory method to check for annotations because that does not get
	 * the annotations added to the bean in the Config environment ie when the Bean annotation is used.
	 * It returns a list of the relevant bean definitions.
	 * @param beanFactory
	 * @param annotationType
	 * @return list of bean definitions
	 */
	public static List<BeanDefinition> beansAnnotatedWith(BeanFactory beanFactory, Class<? extends Annotation> annotationType) {
		List<BeanDefinition> ret = new ArrayList<>();
		if (beanFactory instanceof DefaultListableBeanFactory) {
			DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)beanFactory;
			Iterator<String> it = defaultListableBeanFactory.getBeanNamesIterator();
			while( it.hasNext()) {
				String beanName = it.next();
				BeanDefinition bd;
				try {
					bd = defaultListableBeanFactory.getBeanDefinition(beanName);
				} catch (NoSuchBeanDefinitionException e) {
					continue;
				}
				Annotation a = defaultListableBeanFactory.findAnnotationOnBean(beanName, annotationType);
				if (a != null) {
					// The actual class is annotated so add it to the list
					ret.add(bd);
					continue;
				}
				Object o = bd.getSource();
				if (o instanceof MethodMetadataReadingVisitor) {
					// The annotation is there, just outside the class.
					MultiValueMap<String, Object> mvm = ((MethodMetadataReadingVisitor)o).getAllAnnotationAttributes(annotationType.getName());
					if (mvm != null) {
						ret.add(bd);
						continue;
					}
				}
			}
		}
		return ret;
	}

}
