package com.mrhuman.levi9.ageofarmies.gamecore;

import javax.swing.*;

public class Bullet {
    static int demage = 5;
    private static float GRAVITATION = 10;
	private int cannonX;
	private int cannonY;
	int targetX;
	int targetY;
	private long creationTime;
	private double x;
	private double y;
	private double height;
    private double initialSpeed;
    private double speedX;
    private double speedY;
    private double distance;
    private long lastIteration;


	public Bullet(int cannonX, int cannonY, int targetX, int targetY) {
	    this.cannonX = cannonX;
	    this.cannonY = cannonY;
	    this.targetX = targetX;
	    this.targetY = targetY;
	    lastIteration = creationTime = System.currentTimeMillis();
	    x = cannonX;
	    y = cannonY;
	    height = 0;
	    distance = Math.sqrt(Math.pow(cannonX - targetX, 2) + Math.pow(cannonY - targetY, 2));
	    initialSpeed = Math.sqrt(GRAVITATION * distance);
	    speedX = initialSpeed * Math.sqrt(2) / 2;
        speedY = initialSpeed * Math.sqrt(2) / 2;
    }

	public void step() {
/*	    float t = (System.currentTimeMillis()-creationTime)/1000;
        x =cannonX+t* speedX * (targetX - cannonX) / distance;
        y = cannonY+t*speedX * (targetY - cannonY) / distance;
        height += speedY;
        speedY =  initialSpeed * Math.sqrt(2) / 2 - t*GRAVITATION;
*/
        float timeElapsed = (System.currentTimeMillis()-lastIteration)/1000;
        lastIteration = System.currentTimeMillis();
        x +=speedX * timeElapsed * (targetX - cannonX) / distance;
        y += speedX * timeElapsed * (targetY - cannonY) / distance;
        height += speedY * timeElapsed;
        speedY =  speedY - timeElapsed*GRAVITATION;

    }
	
	public boolean isDestroyed() {
	    return (Math.abs(targetX - cannonX) < 0.01) && (Math.abs(targetY - cannonY) < 0.01) && (height < 0.01);
	}

}
