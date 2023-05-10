import { ACCESS_TOKEN } from "../../constants";

import NavbarAdmin from "./components/NavbarAdmin";
import React from "react";
import CreateRateForm from "./components/CreateRateForm";


function CreateRateAdminPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <NavbarAdmin/>
        <CreateRateForm/>
      </div>
    );
  }
  
  export default CreateRateAdminPage;