ACCMOB.autocomplete = {
		createSuggestionsContainer: function() {
			$('<div id="suggestions_container"><ul id="suggestions" data-ajax="false" data-role="listview" data-inset="true" data-theme="g" data-content-theme="g"></ul></div>').insertAfter('#header');

		},

		setUp: function(searchField) {
			$('div#suggestions_container ul').listview();
			var url   = searchField.data('autocomplete-url');
			var value = searchField.val();

			$('div#suggestions_container ul').listview('refresh');

			searchField.autocomplete({
				target:    $('#suggestions'),
				source:    url,
				link:      ACCMOB.common.contextPath + '/search?text=',
				minLength: 2
			});
		},

		init: function(searchField) {
			ACCMOB.autocomplete.createSuggestionsContainer();

			searchField.live("focusout", function() {
				$("div#suggestions_container ul").empty();
			});
			searchField.live("focusin", function() {
				ACCMOB.autocomplete.setUp($("#search"));
			});
		}

};

$(document).ready(function() {
	ACCMOB.autocomplete.init($("#search"));
});
