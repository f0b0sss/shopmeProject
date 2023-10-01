let dropDownCountry;
let dataListState;
let fieldState;


$(document).ready(function () {
    dropDownCountry = $("#country");
    dataListState = $("#listStates");
    fieldState = $("#state");

    dropDownCountry.on("change", function () {
        loadStatesForCountry();
        fieldState.val("").focus();
    });
});

function loadStatesForCountry() {
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val();
    const url = contextPath + "countries/" + countryId + "/states";

    $.get(url, function (responseJSON) {
        dataListState.empty();

        $.each(responseJSON, function (index, state) {
            console.log("state.name - " + state.name);
            $("<option>").val(state.name).text(state.name).appendTo(dataListState);
        });
    }).fail(function () {
        // alert('Failed to connect to the server!');
    });
}

function checkPasswordMatch(confirmPassword) {
    if (confirmPassword.value != $("#password").val()) {
        confirmPassword.setCustomValidity("Password do not match!");
    } else {
        confirmPassword.setCustomValidity("");
    }
}