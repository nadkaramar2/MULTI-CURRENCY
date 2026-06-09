import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { useNavigate, useParams } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
export default function EditAccountType() {
  const navigate = useNavigate();
  const { strAccountType } = useParams();
  const [alldata2, setalldata2] = useState([]);
  const [alldatatype, setalldatatype] = useState([]);
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

  useEffect(() => {
    handleSubmitView();
  }, []);

  const handleSubmitView = async () => {
    if (strAccountType === "" || strAccountType.length === 0) {
      swal("Please Select Account type");
    } else {
      try {
        const response = await amsApi.post(
          `accountType/getAccountTypeMasterDetailsBasedonAccountType`,
          {
            strAccountType: strAccountType,
          }
        );
        if (response.data.code === "S0000") {
          setalldatatype(response.data.accountTypeMaster2);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  return (
    <AppLayout>
      <div className="min-h-full first-line:flex items-center justify-center border-b sticky top-0">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-sm  sm:text-xm text-black :text-2xl  leading-normal ">
                Edit Account Creation
              </p>
            </div>
          </div>
        </div>

        <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[30rem]">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4 py-1 sm:p-2 bg-white">
                {alldatatype.map((data) => (
                  <div>
                    <div className="grid gap-3 mb-3 md:grid-cols-3">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Type<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          defaultValue={data.strAccountType}
                          // disabled
                          // value={data.strAccountType}
                          // onChange={(e) => setstrAccountType(e.target.value)}
                          id="strAccountType"
                          autoComplete="off"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Number
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDescription}
                          // defaultValue={data.strDescription}
                          // onChange={(e) => setstrDescription(e.target.value)}
                          id="strDescription"
                          autoComplete="off"
                          placeholder="Enter Account Number"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>

                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Credit Limit Category
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAccNumStartDigit}
                          // defaultValue={data.strAccNumStartDigit}
                          // onChange={(e) => setstrAccNumStartDigit(e.target.value)}
                          id="strAccNumStartDigit"
                          autoComplete="off"
                          placeholder="Enter Credit Limit Category "
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>

                    <div>
                      <h2 className="font-normal md:font-bold py-1">
                        Personal Information
                      </h2>
                    </div>
                    <div className="grid gap-4 mb-4 md:grid-cols-5">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Title
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          id="strStatus"
                          name="strStatus"
                          // defaultValue={data.strStatus}
                          // value={strStatus}
                          // onChange={(e) => setstrStatus(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value=""> Select</option>
                          <option value="Mr"> Mr</option>
                          <option value="Mrs">Mrs </option>
                          {/* <option className="captlize text-lg"></option> */}
                        </select>
                      </div>
                    </div>
                    <div className="grid gap-6 mb-6 md:grid-cols-3">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          First Name
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter  First Name"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Middle Name
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter  middle Name"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Last Name
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter Last Name"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Date Of Birth
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter Date Of Birth"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Email Id
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter Email Id"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Mobile Number
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter Mobile Number"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Phone Number
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter Phone Number"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>

                    <div>
                      {""}
                      <h2 className="font-normal md:font-bold py-1">
                        Address Information
                      </h2>
                    </div>
                    <div className="grid gap-4 mb-4 md:grid-cols-3">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Address Line1
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter Address Line1"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Address Line2
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter Address Line2"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Address Line3
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          // defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          placeholder="Enter Address Line3"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Country
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="glAccountType"
                          id="glAccountType"
                          // value={glAccountType}
                          // onChange={(e) => setglAccountType(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option> Select </option>
                          {alldata2.map((data) => (
                            <optgroup key={uuidv4()}>
                              <option
                                className="capatlize text-lg"
                                // value={}
                              >
                                {data.strGLAccountType} -{""}
                                {data.strGLAccountNumber || "-"}
                              </option>
                            </optgroup>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          State
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="glAccountType"
                          id="glAccountType"
                          // value={glAccountType}
                          // onChange={(e) => setglAccountType(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option> State </option>
                          {alldata2.map((data) => (
                            <optgroup key={uuidv4()}>
                              <option
                                className="capatlize text-lg"
                                // value={}
                              >
                                {data.strGLAccountType} -{""}
                                {data.strGLAccountNumber || "-"}
                              </option>
                            </optgroup>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          City
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="glAccountType"
                          id="glAccountType"
                          // value={glAccountType}
                          // onChange={(e) => setglAccountType(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option> Select </option>
                          {alldata2.map((data) => (
                            <optgroup key={uuidv4()}>
                              <option
                                className="capatlize text-lg"
                                // value={}
                              >
                                {data.strGLAccountType} -{""}
                                {data.strGLAccountNumber || "-"}
                              </option>
                            </optgroup>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Postal Code
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAllowLoadCash}
                          // defaultValue={data.strAllowLoadCash}
                          // onChange={(e) => setstrAllowLoadCash(e.target.value)}
                          id="strAllowLoadCash"
                          autoComplete="off"
                          placeholder="Enter Postal Code"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>

                    <div>
                      {""}
                      <h2 className="font-normal md:font-bold py-1">
                        Address Proof
                      </h2>
                    </div>
                    <div className="grid gap-6 mb-6 md:grid-cols-2">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Document Type
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAllowLoadCash}
                          // defaultValue={data.strAllowLoadCash}
                          // onChange={(e) => setstrAllowLoadCash(e.target.value)}
                          id="strAllowLoadCash"
                          autoComplete="off"
                          placeholder="Enter Document Type"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Document Value
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAllowLoadCash}
                          // defaultValue={data.strAllowLoadCash}
                          // onChange={(e) => setstrAllowLoadCash(e.target.value)}
                          id="strAllowLoadCash"
                          autoComplete="off"
                          placeholder="Enter Document Value"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>
                    <div className="grid gap-3 mb-3 md:grid-cols-2">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Document Type
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAllowLoadCash}
                          // defaultValue={data.strAllowLoadCash}
                          // onChange={(e) => setstrAllowLoadCash(e.target.value)}
                          id="strAllowLoadCash"
                          autoComplete="off"
                          placeholder="Enter Document type"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>

                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Document Value
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAllowLoadCash}
                          // defaultValue={data.strAllowLoadCash}
                          // onChange={(e) => setstrAllowLoadCash(e.target.value)}
                          id="strAllowLoadCash"
                          autoComplete="off"
                          placeholder="Enter Document Value"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>
                  </div>
                ))}
              </div>

              <div className="bg-gray-100 px-4 py-2 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  // onClick={txn}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2 px-5  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  // onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2 px-5 mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
                <button
                  title="Go Back"
                  type="button"
                  onClick={() => navigate(-1)}
                  className="text-white bg-blue-500 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-xs px-5 py-2 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700"
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
