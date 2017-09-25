package com.TransactionTemplate.Template;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import com.TransactionTemplate.Template.*;

;

@Component("mainDao")
public class MainDAO
{

	private NamedParameterJdbcTemplate	jdbc;

	@Autowired
	public void setDataSource(DataSource jdbc)
	{
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	public void updatePerson(Person person)
	{
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(person);
		jdbc.update("update users set name =:name,cash= :money,description =:description where id = :id", params);
		System.out.println("Person Updated");
	}

	public List<Person> getPeople()
	{
		MapSqlParameterSource params = new MapSqlParameterSource();
		return jdbc.query("select * from users", params, new RowMapper<Person>()
		{
			public Person mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Person person = new Person();
				person.setId(rs.getInt("id"));
				person.setName(rs.getString("name"));
				person.setMoney(rs.getInt("cash"));
				person.setDescription(rs.getString("description"));
				System.out.println("ID: " + person.getId() + "  Name: " + person.getName() + " Cash: " + person.getMoney() + " Description: "
						+ person.getDescription());
				return person;
			}
		});
	}

	public boolean AddPerson(Person person)
	{
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(person);

		return jdbc.update("insert into users (name, cash, description) values (:name,:money,:description)", params) == 1;
	}

	public boolean addTransaction(Transaction trans)
	{
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(trans);
		System.out.println("transaction added");
		return jdbc.update("insert into transactions (seller, buyer, amount, description) values (:person2Id,:person1Id,:amount,:description)",
				params) == 1;
	}

	public Person getPerson(int id)
	{
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		return jdbc.queryForObject("select * from users where id = :id", params, new RowMapper<Person>()
		{
			public Person mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Person person = new Person();
				person.setId(rs.getInt("id"));
				person.setName(rs.getString("name"));
				person.setMoney(rs.getInt("cash"));
				person.setDescription(rs.getString("description"));
				System.out.println("ID: " + person.getId() + "  Name: " + person.getName() + " Cash: " + person.getMoney() + " Description: "
						+ person.getDescription());
				return person;
			}
		});
	}

	public void deletePerson(int id)
	{
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		jdbc.update("delete from users where id = :id", params);
	}

	public boolean transaction(int personid1, int personid2, int amt, String description)
	{
		// Retreive People from database
		Person person1 = getPerson(personid1);
		Person person2 = getPerson(personid2);
		Transaction trans = new Transaction(personid1, personid2, amt, description);
		// check if transaction is viable
		if (person1.getMoney() - amt > 0)
		{
			System.out.println("Enough money present. Transcation Continues");
			int money1 = person1.getMoney();
			person1.setMoney(money1 -= amt);
			int money2 = person2.getMoney();
			person2.setMoney(money2 += amt);

			updatePerson(person1);
			System.out.println("Person1 Updated");

			updatePerson(person2);
			System.out.println("Person2 Updated");

			addTransaction(trans);

		}
		else System.out.println("Insufficcent Funds");

		return false;

	}
}
