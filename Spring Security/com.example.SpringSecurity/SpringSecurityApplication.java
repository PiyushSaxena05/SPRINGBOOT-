package com.example.SpringSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

}

/*
Assets ->valuable resources
|
Data Assets ->application related data
|
operational assets -> operation that can be performed
|
security assets ->session id and all
|
Infra Assets -> databases, application, servers etc.
|
Availability -> application is up to how much time



Threat - intentional harm

Vulnerability - weak point of our application on which hacker
can create threat


Threat + vulnerability = Attack


Authentication
This means acceptation or rejection of a claim.

Suppose, I have written my name is
Piyush

so, the site will provide some username and password
on the basis of which my claim will be accepted or rejected.


What is Authorization??
This means after Authentication we can enter the application
but, to which all parts we can interact will tell
our authorization, or we can say what is our role
example:- admin,user,manager etc.
In instagram we have access to our own accounts,
and we can handle our post stories etc.


Authentication:- it is divided into 3 parts


1) something we know
password
pin
security answer


2)something you have
smart card
Authenticator app
cryptographic key


3)something you are
Fingerprint
Iris
Face


MFA (MULTI-FACTOR AUTHENTICATION APP)


Authorization-> Role


Identity
Credentials
Principal
Authorities
Roles
Permissions
Authentication
SecurityContext
SecurityContextHolder


Identity - User, Customer, Scheduled Job
It is an entity whose action is to be recognized.

Credentials- To prove identity, credentials are provided
E.g.- username, otp, password.
+
Principal - current or active identity
Suppose if I am verifying any person
so that the particular latest person is called as principal.

Authorities
what all things can be accessed.

Role means responsibility - which all authorities a person can have,
e.g. - Admin can have multiple authorities.


Authentication

Authentication Request

principal = "latest user"
credentials = "raw-password"
authorities = []
authenticated = false


Authentication Principal
principal = UserDetails("latest user")
credentials = null
authorities = [COURSE_READ,COURSE_UPDATE]
authenticated = true


Authentication states

Stateful Authentication
Stateless Authentication

                         Application    |          Redis(central cache)
                        /
client -> load balancer -> Application  |   => Authentication
 |    /login             \
 |    ""                   Application''|
 sessions id:123
 now, suppose if session id 123 is requested to any application
 so, data can be fetched from central cache regarding that particular session id.



client ----> application  ---> token service
/getUser()
token : <opaque>

self-contained has all the information of the user
whereas opaque does not have full information like
self-contained token.

with self-contained we can fetch all the info but
in opaque that token is to be given to token service
and token service will give us an authentication object....



Bearer token: trh2343adsa some random value

when someone is sending a bearer token is the one who has
self owned this token...the token is self-contained, but sometimes
it can be opaque


in stateful, an old authentication object is used
whereas in a stateless new object is created, and this is the main
difference.





 */
