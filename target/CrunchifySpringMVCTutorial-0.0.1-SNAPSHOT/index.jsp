<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<br>
	<div style="text-align: center">
		<h2>
			Standard Checkout<br> <br>
		</h2>
		<script type="text/javascript"
			src="https://sandbox.static.masterpass.com/integration/merchant.js"></script>
		<a href="#" onClick="launchUI()"> <img
			src="https://www.mastercard.com/mc_us/wallet/img/en/US/mcpp_wllt_btn_chk_147x034px.png" />
		</a>


	</div>
	<div style="text-align: center">
		<h2>
			Hey You..!! This is your 1st Spring MCV Tutorial..<br> <br>
		</h2>
		<h3>
			<a href="welcome.html">Click here to See Welcome Message... </a>(to
			check Spring MVC Controller... @RequestMapping("/welcome"))
		</h3>
	</div>
	<script type="text/javascript">
		function launchUI() {// merchant configuration parameters 

			const
			myConfig = {
				checkoutId : 'e60e6400d7ce41e1979bc8930819bca3',
				cartId : '6a7804b7-6e6f-4ec3-ac1c-f7951704082c',
				//currencyId: 'usd', 
				//subtotal: '100.00' ,
				allowedCardTypes : [ "master", "amex", "diners", "discover",
						"jcb", "maestro", "visa" ],
				amount : "1",
				currency : "USD",

			}
			// launch masterpass UI 
			masterpass.checkout(myConfig);
		}
	</script>
</body>
</html>