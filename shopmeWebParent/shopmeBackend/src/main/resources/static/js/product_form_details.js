$(document).ready(function () {
    $("a[name='linkRemoveDetail']").each(function (index) {
        $(this).click(function () {
            removeDetailSectionByIndex(index);
        });
    });
});

function addNextDetailSection() {
    allDivDetails = $("[id^='divDetail']");
    divDetailsCount = allDivDetails.length;

    htmlDetailSection = `
    <div class="row" id="divDetail${divDetailsCount}">
        <input type="hidden" name="detailIds" value="0">
        <div class="col">
            <label class="m-3">Name:</label>
            <input type="text" class="form-control w-50" name="detailNames" th:field="*{name}" maxlength="255">
        </div>
        <div class="col">
            <label class="m-3">Value:</label>
            <input type="text" class="form-control w-50" name="detailValues" th:field="*{name}" maxlength="255">
        </div>
    </div>
    `

    $("#divProductDetails").append(htmlDetailSection);

    previousDivDetailSection = allDivDetails.last();
    previousDivDetailId =  previousDivDetailSection.attr("id");

    htmlLinkRemove = `
        <a class="btn fas fa-times-circle fa-2x icon-dark float-end"
        href="javascript:removeDetailSectionId('${previousDivDetailId}')" 
        title="Remove this detail"></a>
    `;


    previousDivDetailSection.append(htmlLinkRemove);

    $("input[name='datailNames']").last().focus();
}

function removeDetailSectionId(id) {
    $("#" + id).remove();
}

function removeDetailSectionByIndex(index) {
    $("#divDetail" + index).remove();
}