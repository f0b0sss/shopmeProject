let buttonLoadForStates;
let dropDownCountryForStates;
let dropDownStates;
let buttonAddState;
let buttonUpdateState;
let buttonDeleteState;
let labelStateName;
let fieldStateName;

$(document).ready(function () {
    buttonLoadForStates = $("#buttonLoadCountriesForStates");
    dropDownCountryForStates = $("#dropDownCountriesForStates");
    dropDownStates = $("#dropDownStates");
    buttonAddState = $("#buttonAddState");
    buttonUpdateState = $("#buttonUpdateState");
    buttonDeleteState = $("#buttonDeleteState");
    labelStateName = $("#labelStateName");
    fieldStateName = $("#fieldStateName");

    buttonLoadForStates.click(function () {
        loadCountriesForStates();
    });

    dropDownCountryForStates.on("change", function () {
       loadStatesForCountry();
    });

    dropDownStates.on("change", function () {
        changeFormStateToSelectState();
    });

    buttonAddState.click(function () {
        if (buttonAddState.val() === "Add"){
            addState();
        }else {
            changeFormStateToNewStates();
        }
    });

    buttonUpdateState.click(function () {
        updateState();
    });

    buttonDeleteState.click(function () {
        deleteState();
    });
});

function loadStatesForCountry() {
    let  selectedCountry = $("#dropDownCountriesForStates option:selected");
    let countryId = selectedCountry.val();

    let url = contextPath + "countries/" + countryId + "/states";

    $.get(url, function (responseJSON) {
        dropDownStates.empty();

        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        });
    }).done(function () {
        changeFormStateToNewStates();
        showToastMessage("All states have been loaded for country " + selectedCountry.text());
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function loadCountriesForStates() {
    let  url = contextPath + "countries";

    $.get(url, function (responseJSON) {
        dropDownCountryForStates.empty();

        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountryForStates);
        });
    }).done(function () {
        buttonLoadForStates.val("Refresh Country List");
        showToastMessage("All countries have been loaded");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function changeFormStateToSelectState() {
    buttonAddState.prop("value", "New");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    labelStateName.text("Selected State/Province:");

    let selectedStateName = $("#dropDownStates option:selected").text();
    fieldStateName.val(selectedStateName);
}

function addState() {
    let url = contextPath + "states/save";
    let stateName = fieldStateName.val();

    let selectedCountry = $("#dropDownCountriesForStates option:selected");
    let countryId = selectedCountry.val();
    let countryName = selectedCountry.text();

    let jsonData = {name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (stateId) {
        selectNewlyState(stateId, stateName)
        showToastMessage("The new State has been added")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function changeFormStateToNewStates() {
    buttonAddState.val("Add");
    labelStateName.text("State/Province Name:");

    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);

    fieldStateName.val("").focus();
}

function selectNewlyState(stateId, stateName) {
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

    $("#dropDownStates option[value='" + stateId + "']").prop("selected", true);

    fieldStateName.val("").focus();
}

function updateState() {
    let url = contextPath + "states/save";
    let stateId = dropDownStates.val();
    let stateName = fieldStateName.val();

    let selectedCountry = $("#dropDownCountriesForStates option:selected");
    let countryId = selectedCountry.val();
    let countryName = selectedCountry.text();

    let jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function () {
        $("#dropDownStates option:selected").text(stateName);
        showToastMessage("The state has been updated")
        changeFormStateToNewStates();
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function deleteState() {
    let stateId = dropDownStates.val();

    let url = contextPath + "states/delete/" + stateId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        $("#dropDownStates option[value='" + stateId + "']").remove();
        changeFormStateToNewStates();
        showToastMessage("The state has been deleted");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

