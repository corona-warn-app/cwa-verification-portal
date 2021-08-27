(() => {
  $(document).ready(() => {
    const healthAuthorityInput = $("#health-authority");
    const healthAuthorityIdInput = $("#health-authority-id");
    const healthAuthorityDataList = $("#health-authorities");
    const piwTanButton = $("#piw-tan-button");

    piwTanButton.prop("disabled", true);

    const healthAuthorities = JSON.parse(healthAuthorityRawData);
    fillHealthAuthorityDataList(healthAuthorities, healthAuthorityDataList);

    healthAuthorityInput.on("change keyup", () => {
      const foundId = healthAuthorities.find(ha => ha.name ===  healthAuthorityInput.val());

      if (foundId) {
        piwTanButton.prop("disabled", false);
        healthAuthorityIdInput.val(foundId.nr);
      } else {
        piwTanButton.prop("disabled", true);
      }
    })
  })

  function fillHealthAuthorityDataList(healthAuthorities, healthAuthorityDataList) {
    healthAuthorities.forEach((healthAuthority => {
      const newElement = $("<option></option>");
      newElement.text(healthAuthority.name);
      healthAuthorityDataList.append(newElement);
    }));
  }
})();
