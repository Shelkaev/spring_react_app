import Navbar from "./components/Navbar";
import React from "react";

function NotFoundPage() {
    return (
      <div>
      <Navbar/>

        <section id="login">
        <div className="container mt-5">
          <div className="row">
            <div className="col-md-6 mx-auto">
              <div className="card">
                <div className="card-header">
                  <h4 className = "text-center">Страница не найдена</h4>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      </div>
    );
}

export default NotFoundPage;