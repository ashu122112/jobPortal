package com.jobportal.Job.Portal.utility;


import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.jobportal.Job.Portal.entity.Sequence;
import com.jobportal.Job.Portal.exception.JobPortalException;
@Component
public class Utilities {
	private static MongoOperations mongoOperation;//static field cannot be injected via @Autowired, need to inject using setter injection
	@Autowired//if we are using Autowired above a setter means we are doing setter injection
	public void setMongoOperation(MongoOperations mongoOperation)
	{
		Utilities.mongoOperation=mongoOperation;
	}
	
	public static Long getNextSequence(String key) throws JobPortalException
	{
		Query query=new Query(Criteria.where("_id").is(key));
		Update update=new Update();
		update.inc("seq",1);
		FindAndModifyOptions options =new FindAndModifyOptions();
		options.returnNew(true);
		Sequence seq=mongoOperation.findAndModify(query, update, options,Sequence.class);
		if(seq==null) throw new JobPortalException("Unable to get sequence id for key: "+key);
		return seq.getSeq();
	}

	public static String generateOTP() {
		StringBuilder otp = new StringBuilder();
		SecureRandom secureRandom = new SecureRandom();
		for (int i = 0; i < 6; i++) {
			otp.append(secureRandom.nextInt(10));
		}
		return otp.toString();
	}
}
