import { ACCESS_TOKEN } from "../../constants";

import NavbarAdmin from "./components/NavbarAdmin";
import React from "react";
import AdminCreditCard from "./components/AdminCreditCard";


function CreditCardAdminPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <NavbarAdmin/>
        <AdminCreditCard/>
      </div>
    );
  }
  
  export default CreditCardAdminPage;