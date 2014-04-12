package com.scoreproject.service.impl;

import java.util.List;

import com.daoSupport.dao.impl.EntityDaoImpl;
import com.daoSupport.exception.DBException;
import com.daoSupport.exception.ErrorException;
import com.scoreproject.po.Class;
import com.scoreproject.service.ClassService;

public class ClassServiceImpl implements ClassService {

	private ClassServiceImpl() {

	}

	private static ClassServiceImpl data = new ClassServiceImpl();

	public static ClassServiceImpl getInstance() {
		if (data == null) {
			synchronized (ClassServiceImpl.class) {
				return new ClassServiceImpl();
			}
		} else {
			return data;
		}

	}

	@Override
	public void addClass(String classx) {
		Class calss = new Class();
		calss.setClassNo(classx);
		try {
			EntityDaoImpl.getInstance().save(calss);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// findAllEntity（Work.class,0,100000,null,null,null,0）；
	@Override
	public List<Class> showClass() {
		List<Class> result = null;
		try {
			result = EntityDaoImpl.getInstance()
					.findAllEntity(Class.class, 0, 100000, null, null, null, 0)
					.getResults();
		} catch (ErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
