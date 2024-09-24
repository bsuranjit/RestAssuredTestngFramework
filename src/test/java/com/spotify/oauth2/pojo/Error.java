package com.spotify.oauth2.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter @Setter
//@Jacksonized
//@Builder

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {
	//@JsonInclude(JsonInclude.Include.NON_NULL)
	
	//@Generated("jsonschema2pojo")
	

	@JsonProperty("error")
	private InnerError error;

	/*@JsonProperty("error")
	public InnerError getError() {
	return error;
	}

	@JsonProperty("error")
	public void setError(InnerError error) {
	this.error = error;
	}*/

	}


