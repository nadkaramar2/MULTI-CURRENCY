import React, { useState } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";
export default function EditCurrency() {
  const navigate = useNavigate();
  const userId = localStorage.getItem("userName");
  const { country } = useParams();
  const [baseCountry, setbaseCountry] = useState("");
  const [status, setstatus] = useState("");
  const [error, setError] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  // save form
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (baseCountry === "" || baseCountry.length === 0 || status === "") {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(`currency-master/updateCurrency`, {
          country: country,
          baseCountry: baseCountry,
          status: status,
          createdBy: userId,
        });

        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowSuccess(response.data.message);
        }
      } catch (error) {
        handleShowSuccess(error.response.data.message);
      }
    }
  };

  const handleClick = () => {
    setstatus("");
    setbaseCountry("");
  };
  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md ">
          <div className="px-4 sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-sm text-black :text-2xl  leading-normal ">
                Edit Currency
              </p>
            </div>
          </div>
        </div>
        <div className="md:col-span-2 lg:col-span-1 ">
          <form className="">
            <div className="overflow-hidden shadow">
              <div className=" px-4 sm:p-2 bg-white ">
                <div className="grid gap-6  px-8  md:grid-cols-3 ">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-sm font-semibold text-gray-700"
                    >
                      Base Country<span className="text-red-600">*</span>
                    </label>
                    <select
                      id="baseCountry"
                      name="baseCountry"
                      value={baseCountry}
                      onChange={(e) => setbaseCountry(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value=""> Select </option>
                      <option value="Y"> Yes</option>
                      <option value="N">No</option>
                    </select>
                    {error && baseCountry.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Select Base Country
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-sm font-semibold text-gray-700"
                    >
                      Status<span className="text-red-600">*</span>
                    </label>
                    <select
                      id="status"
                      name="status"
                      value={status}
                      onChange={(e) => setstatus(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value=""> Select </option>
                      <option value="Active"> Active </option>
                      <option value="InActive">InActive</option>
                    </select>
                    {error && status.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Select Status
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="button"
                  data-modal-toggle="defaultModal"
                  onClick={handleSubmit}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1 px-4  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1 px-4 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
                <button
                  title="Go Back"
                  type="button"
                  onClick={() => navigate(-1)}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1 px-4 mx-4 text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  BACK
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
