function setupValidation() {
	jQuery.validator.addMethod("postalcode", function(postalcode, element) {
		return this.optional(element) || postalcode.match(/(^\d{5}(-\d{4})?$)|(^[ABCEGHJKLMNPRSTVXYabceghjklmnpstvxy]{1}\d{1}[A-Za-z]{1} ?\d{1}[A-Za-z]{1}\d{1})$/);
	}, "Please specify a valid postal/zip code");
	jQuery.validator.addMethod("password", function( value, element ) {
		return this.optional(element) || value.length >= 6 && /\d/.test(value) && /[a-z]/i.test(value);
	}, "Please follow the password guide-lines");
	jQuery.validator.addMethod("phone", function(phone, element) {
		return this.optional(element) || phone.match(/^(\+\d)*\s*(\(\d{3}\)\s*)*\d{3}(-{0,1}|\s{0,1})\d{2}(-{0,1}|\s{0,1})\d{2}$/);
	}, "Please specify a valid phone");
	jQuery.validator.addMethod("fax", function(fax, element) {
		return this.optional(element) || fax.match(/([\(\+])?([0-9]{1,3}([\s])?)?([\+|\(|\-|\)|\s])?([0-9]{2,4})([\-|\)|\.|\s]([\s])?)?([0-9]{2,4})?([\.|\-|\s])?([0-9]{4,8})/);
	}, "Please specify a valid fax");
	jQuery.validator.addMethod("website", function(website, element) {
		return this.optional(element) || website.match(/(http:\/\/|)(www\.)?([^\.]+)\.(\w{2}|(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum))$/);
	}, "Please specify a valid website");
	jQuery.validator.addMethod("birthdate", function(birthdate, element) {
		return this.optional(element) || birthdate.match(/^(([1-9])|(0[1-9])|(1[0-2]))\/(([0-9])|([0-2][0-9])|(3[0-1]))\/([1-2][0,9][0-9][0-9])$/);
	}, "Please specify a valid birthdate in MM/dd/YYYY format");
}

