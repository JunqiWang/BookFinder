package com.wilddynamos.bookfinder.dblayout;

import android.provider.BaseColumns;

public final class RememberMeContract {
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	public RememberMeContract() {
	}

	/* Inner class that defines the table contents */
	public static abstract class RememberMeColumn implements BaseColumns {
		public static final String TABLE_NAME = "Remember_Me";
		public static final String COLUMN_NAME_C1 = "email";
		public static final String COLUMN_NAME_C2 = "password";

	}
}