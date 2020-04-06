package com.fabiani.domohome;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

//POJO

public class Command {
	static final Integer[] sWhereChoices = new Integer[99]; //TODO: Check boundaries
	static final Integer[] sTimeOutChoices={0,1,2,3,4,5};
	private UUID mId;
	private String mTitle;
	private transient int mWhat;
	private int mWho;
	private int mWhere;
	private int mTimeout;

	public enum WhoChoice 	{SCENARIOS(0), LIGHTING(1),AUTOMATISM(2), MH200N_SCENARIOS(17);

		private  int mValue;

		WhoChoice(int value){
			mValue=value;
		}

		public int getValue() {
			return mValue;
		}
	}

	public Command() {
		mId = UUID.randomUUID();
		Arrays.setAll(sWhereChoices, i -> i + 1);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Command command = (Command) o;
		return mWhat == command.mWhat &&
				mWho == command.mWho &&
				mWhere == command.mWhere &&
				mTimeout == command.mTimeout &&
				mId.equals(command.mId) &&
				mTitle.equals(command.mTitle);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mId, mTitle, mWhat, mWho, mWhere, mTimeout);
	}

	@Override
	public String toString() {
		return mTitle;
	}

	//getters and setters
	public UUID getId() {
		return mId;
	}

	public void setTitle(String title) {
			mTitle=title;
	}

	public String getTitle() {
		return mTitle;
	}

	void setWho(int who) {
		mWho = who;
	}

	int getWho() {
		return mWho;
	}

	int getWhat() {
		return mWhat;
	}

	void setWhat(int what) {
		mWhat = what;
	}

	int getWhere() {
		return mWhere;
	}

	void setWhere(int where) {
		mWhere = where;
	}

	int getTimeout() {
		return mTimeout;
	}

	void setTimeout(int timeout) {
		mTimeout = timeout;
	}
}
