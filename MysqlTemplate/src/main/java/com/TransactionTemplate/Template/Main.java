package com.TransactionTemplate.Template;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.dao.DataAccessException;

//import com.TransactionTemplate.Template.*;

public class Main
{

	public static void main(String[] args)
	{
		ApplicationContext context = new FileSystemXmlApplicationContext("beans.xml");
		MainDAO mainDao = (MainDAO) context.getBean("mainDao");
		// addUsers(mainDao);
		try
		{
			mainDao.getPeople();
			mainDao.transaction(1, 2, 69, "Purchase of 69 value");
		}
		catch (DataAccessException e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}

	public static void addUsers(MainDAO mainDao)
	{
		try
		{
			Person p1 = new Person("Dave", 25000, "Business");
			Person p2 = new Person("Jeremey", 3500, "Customer");

			if (mainDao.AddPerson(p1))
			{
				System.out.println("Created Person Object" + p1);
			}
			if (mainDao.AddPerson(p2))
			{
				System.out.println("Created Person Object" + p2);
			}
		}
		catch (DataAccessException e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}
	
	
	
	// MakeTransaction(buyerid,Sellerid,ammount,description)
}
