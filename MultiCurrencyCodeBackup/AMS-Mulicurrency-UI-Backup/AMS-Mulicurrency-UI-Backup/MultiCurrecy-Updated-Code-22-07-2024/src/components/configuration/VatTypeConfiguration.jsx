import amsApi from "../../api/amsApi";
import AppLayout from "../../layout/AppLayout";
import React, { useState } from "react";
import Swal from "sweetalert2";
import CustomAlert from "../../layout/CustomAlert";
export default function VatTypeConfiguration() {
  const [vatType, setvatType] = useState("");
  const [VatDescription, setVatDescription] = useState("");
  const [status, setstatus] = useState("");
  const [error, setError] = useState("");
  const userId = localStorage.getItem("userName");
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
  const vatSubmit = async (event) => {
    event.preventDefault();
    if (vatType === "" || VatDescription === "" || status === "") {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(`vatTypeMaster/addVatTypeMapping`, {
          vatType: vatType,
          vatDescription: VatDescription,
          status: status,
          strCreatedBy: userId,
        });

        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          setvatType("");
          setVatDescription("");
          setstatus("");
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  const handleClick = () => {
    setvatType("");
    setVatDescription("");
    setstatus("");
    setError("");
  };
  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                VAT Type Configuration
              </p>
            </div>
          </div>
        </div>
        <div className=" bg-white sm:rounded-md">
          <form className="" onSubmit={vatSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className="px-4  sm:p-2 bg-white">
                <div className="grid gap-2  lg:grid-cols-1 md:grid-cols-1 px-8 ">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-10 text-base font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Vat Type<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="vatType"
                      value={vatType}
                      onChange={(e) => setvatType(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Vat Type"
                      autoComplete="off"
                    />
                    {error && vatType.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Vat Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-2 py-1 text-base font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Vat Description
                      <span className="text-red-600 ">*</span>
                    </label>
                    <input
                      type="text"
                      id="VatDescription"
                      name="vatDescription"
                      value={VatDescription}
                      disabled={!vatType}
                      onChange={(e) => setVatDescription(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Vat Description"
                      autoComplete="off"
                    />
                    {error && VatDescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Vat Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-7 py-1 text-base font-normal text-gray-700 bg-white"
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Vat Status
                      <span className="text-red-600">*</span>
                    </label>
                    <select
                      id="status"
                      name="status"
                      value={status}
                      disabled={!VatDescription}
                      onChange={(e) => setstatus(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value=""> Select </option>
                      <option value="A"> Active </option>
                      <option value="I">InActive</option>
                    </select>
                    {error && status.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Vat Status
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-5  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-5 mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
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
