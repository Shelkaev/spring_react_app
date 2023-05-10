import { ACCESS_TOKEN } from "../../constants";

import NavbarAdmin from "./components/NavbarAdmin";
import React from "react";
import PenniCreditCardAdmin from "./components/PenniCreditCardAdmin";


function PenniCreditCardAdminPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <NavbarAdmin/>
        <PenniCreditCardAdmin/>
      </div>
    );
  }
  
  export default PenniCreditCardAdminPage;