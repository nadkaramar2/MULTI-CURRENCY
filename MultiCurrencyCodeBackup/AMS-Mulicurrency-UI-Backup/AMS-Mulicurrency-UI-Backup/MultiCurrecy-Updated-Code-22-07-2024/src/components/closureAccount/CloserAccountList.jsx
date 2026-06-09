import React, { useEffect, useState } from "react";
import CustomAlert from "../../layout/CustomAlert";
import amsApi from "../../api/amsApi";
import AppLayout from "../../layout/AppLayout";
import { useNavigate } from "react-router-dom";

export default function CloserAccountList() {
  const accessToken = localStorage.getItem("token");
  console.log("abc" + accessToken);
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertType, setAlertType] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  let navigate = useNavigate();
  const [alldata, setAlldata] = useState([]);
  console.log(alldata);
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
    handleSubmit();
  }, []);

  const handleSubmit = async () => {
    try {
      const response = await amsApi.get(
        `accountclouser/accountmasterclouserlist`,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.listOfAccountClousers);
        // setCheckpoint1(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full scale-96">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Close Account List
              </p>
            </div>
          </div>
        </div>
        {/* table */}
        <div className="overflow-x-auto relative shadow-md py-1 ">
          <>
            <div className="table-wrp block max-h-[43rem] ">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <tr>
                    <th scope="col" className="py-2.5  px-4">
                      CLOSURE REQUEST DATE
                    </th>
                    <th scope="col" className="py-2.5  px-4">
                      CLOSURE REQUEST TIME
                    </th>
                    <th scope="col" className="py-2.5  px-4">
                      ACCOUNT TYPE
                    </th>
                    <th scope="col" className="py-2.5  px-4">
                      ACCOUNT NO
                    </th>
                    <th scope="col" className="py-2.5  px-4">
                      ACCOUNT NAME
                    </th>
                    <th scope="col" className="py-2.5  px-4">
                      CUST ID
                    </th>

                    <th scope="col" className="py-2.5  px-4">
                      ACCOUNT CLOSE
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {alldata.map(
                    ({
                      strClouserReqDate,
                      strClouserReqTime,
                      strAccountType,
                      strAccountNumber,
                      accountHolderName,
                      strCustId,
                    }) => (
                      <tr className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400">
                        <td className="px-4 py-2.5  whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {strClouserReqDate}
                          </span>
                        </td>
                        <td className="px-4 py-2.5  whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {strClouserReqTime}
                          </span>
                        </td>
                        <td className="px-4 py-2.5  whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {strAccountType}
                          </span>
                        </td>
                        <td className="px-4 py-2.5  whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {strAccountNumber}
                          </span>
                        </td>
                        <td className="px-4 py-2.5  whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {accountHolderName}
                          </span>
                        </td>
                        <td className="px-4 py-2.5  whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {strCustId}
                          </span>
                        </td>

                        <td className="px-4 py-2.5  whitespace-nowrap">
                          <span className="text-sm font-medium text-blue-600">
                            <button
                              type="button"
                              onClick={() =>
                                navigate("/accountcloserequest", {
                                  state: {
                                    accountHolderName,
                                    strCustId,
                                    strAccountNumber,
                                  },
                                })
                              }
                            >
                              {""}
                              <span text={"red"}>Click here to link</span>
                            </button>
                          </span>
                        </td>
                      </tr>
                    )
                  )}
                </tbody>
              </table>
            </div>
          </>
        </div>
      </div>
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
