import { ACCESS_TOKEN } from "../../constants";

import NavbarAdmin from "./components/NavbarAdmin";
import React from "react";
import OrderedCreditCardsTable from "./components/OrderedCreditCardsTable";
import ChangePenniTable from "./components/ChangePenniTable";


function CreditMainAdminPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <NavbarAdmin/>
        <OrderedCreditCardsTable/>
        <ChangePenniTable/>
      </div>
    );
  }
  
  export default CreditMainAdminPage;