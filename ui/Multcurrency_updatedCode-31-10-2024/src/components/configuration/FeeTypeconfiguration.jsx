import amsApi from "../../api/amsApi";
import AppLayout from "../../layout/AppLayout";
import React, { useState, useEffect } from "react";
import CustomAlert from "../../layout/CustomAlert";
export default function FeeTypeconfiguration() {
  const [error, setError] = useState("");
  const [alldata, setalldata] = useState([]);
  const [feeType, setfeeType] = useState("");
  const [feeDescription, setfeeDescription] = useState("");
  const [vatType, setvatType] = useState("");
  const [status, setstatus] = useState("");
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
  // Vat Type List
  useEffect(() => {
    vattypelist();
  }, []);

  const vattypelist = async () => {
    try {
      const response = await amsApi.post(`vatTypeMaster/getVatTypeList`, {});

      if (response.data.code === "S0000") {
        setalldata(response.data.vatCollectedList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // save form
  const feetypeSubmit = async (event) => {
    event.preventDefault();
    if (
      feeType === "" ||
      feeDescription === "" ||
      vatType === "" ||
      status === "  "
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(`feeTypeMaster/addFeeTypeMapping`, {
          feeType: feeType,
          feeDescription: feeDescription,
          vatType: vatType,
          status: status,
          strCreatedBy: userId,
        });

        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          setfeeType("");
          setfeeDescription("");
          setvatType("");
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
    setfeeType("");
    setfeeDescription("");
    setvatType("");
    setstatus("");
  };

  // demo

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-sm text-black :text-2xm  leading-normal py-1">
                Fee Type Configuration
              </p>
            </div>
          </div>
        </div>
        <div className=" bg-white sm:rounded-md">
          <form className="" onSubmit={feetypeSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className="px-4 py-2 sm:p-2 bg-white">
                <div className="grid gap-2  lg:grid-cols-1 md:grid-cols-1 px-8">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-12 py-1 text-base font-normal text-gray-700 bg-white  "
                    >
                      Fee Type<span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="feeType"
                      value={feeType}
                      onChange={(e) => setfeeType(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Fee Type"
                      autoCapitalize="off"
                    />
                    {error && feeType.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Fee Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-1 text-base font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Fee Description{" "}
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      id="feeDescription"
                      name="feeDescription"
                      autoCapitalize="off"
                      value={feeDescription}
                      disabled={!feeType}
                      onChange={(e) => setfeeDescription(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Fee  Description"
                      autoComplete="off"
                    />
                    {error && feeDescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-11 text-base font-normal text-gray-700 bg-white  "
                    >
                      Fee Status
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <select
                      id="status"
                      name="status"
                      autoCapitalize="off"
                      value={status}
                      disabled={!feeDescription}
                      onChange={(e) => setstatus(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      <option value="A">Active</option>
                      <option value="I"> InActive</option>
                    </select>
                    {error && status.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Fee Status
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-12 text-base font-normal text-gray-700 bg-white  "
                    >
                      Vat Type{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      name="vatType"
                      id="vatType"
                      autoCapitalize="off"
                      value={vatType}
                      disabled={!status}
                      onChange={(e) => setvatType(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata.map((data) => (
                        <option
                          className="capatlize text-sm"
                          value={data.vatType}
                        >
                          {data.vatType || ""} - {data.vatDescription || ""}
                        </option>
                      ))}
                    </select>
                    {error && vatType.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Vat Type
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
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-5  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  button
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
