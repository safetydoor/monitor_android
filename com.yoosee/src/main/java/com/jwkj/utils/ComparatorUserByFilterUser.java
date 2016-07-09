package com.jwkj.utils;

import java.util.Comparator;

import com.jwkj.data.Contact;

public class ComparatorUserByFilterUser implements Comparator {
	private String searchKey;

	public ComparatorUserByFilterUser(String searchKey) {
		this.searchKey = searchKey;
	}

	@Override
	public int compare(Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		Contact user1 = (Contact) arg1;
		Contact user2 = (Contact) arg2;
		String account1 = user1.contactId;
		String account2 = user2.contactId;
		int index1 = account1.indexOf(searchKey);
		int index2 = account2.indexOf(searchKey);

		if (index1 < index2) {
			return -1;
		} else if (index1 > index2) {
			return 1;
		} else {
			return 0;
		}
	}

}
