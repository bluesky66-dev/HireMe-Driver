package com.adaptor;

public class Movie {
	private String riderid,drivername, ridername;
private String time,profile,distance,pickup,drop,paypal;


	public Movie() {
		
	}

	public Movie(String riderid,String time,String pickup,String drop ,String profile,String drivername,
				 String distance,String ridername,String paypal) {
		
		
		this.riderid=riderid;
		this.time=time;
		this.distance=distance;
	   this.pickup=pickup;
		this.drop=drop;
		this.drivername=drivername;
		this.ridername=ridername;
		this.profile=profile;
		this.paypal=paypal;
		
	}
	 //Detailed list space
	 
	public String getid() {
		return riderid;
	}
	public void setid(String riderid) {
		this.riderid = riderid;
	}

	//*********************************
	
	public String gettime() {
		System.out.println("value of rating "+time);
		return time;
	}
	public void settime(String time) {
		this.time = time;
	}
//*****address****************************	
	
	
	
	
	

//**************bg_image*******************
	public String getridername() {
		return ridername;
	}
	 
	public void setridername(String ridername) {
		this.ridername = ridername;
		
	}
	
//**************profile image*******************
	public String getdrivername() {
		return drivername;
	}
	 
	public void setdrivername(String drivername) {
		this.drivername= drivername;
	}
	//**************status*******************
		public String getprofile() {
			return profile;
		}
		 
		public void setprofile(String profile) {
			this.profile= profile;
		}
//**************status*******************

	public String getdistance() {
		return distance;
	}
	public void setdistance(String distance) {
		this.distance = distance;
	}

	//*********************************

	public String getpickup() {
		return pickup;
	}
	public void setpickup(String pickup) {this.pickup =pickup;
	}

	//*********************************

	public String getdrop() {
		return drop;
	}
	public void setdrop(String drop) {
		this.drop= drop;
	}





}
