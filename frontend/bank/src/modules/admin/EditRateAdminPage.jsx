import { ACCESS_TOKEN } from "../../constants";

import NavbarAdmin from "./components/NavbarAdmin";
import React from "react";
import EditRateForm from "./components/EditRateForm";


function EditRateAdminPage(props) {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <NavbarAdmin/>
        <EditRateForm rateId = {props.id}/>
      </div>
    );
  }
  
  export default EditRateAdminPage;