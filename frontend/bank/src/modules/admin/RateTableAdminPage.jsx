import { ACCESS_TOKEN } from "../../constants";

import NavbarAdmin from "./components/NavbarAdmin";
import React from "react";
import RateTable from "./components/RateTable";


function RateTableAdminPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <NavbarAdmin/>
        <RateTable/>
      </div>
    );
  }
  
  export default RateTableAdminPage;