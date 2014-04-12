package com.scoreproject.service;

import java.util.List;

import com.scoreproject.po.Score;

public interface ScoreService {
	public void addScore(int studentId,int courseId,float score);
	
	public List<Score> showScore(int studentId,int courseId);
	
	
}
