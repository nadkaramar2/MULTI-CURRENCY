import AppLayout from "../../layout/AppLayout";
import React, { useState, useEffect } from "react";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";

import CustomAlert from "../../layout/CustomAlert";

export default function ViewUser() {
  const [alldata, setAlldata] = useState([]);
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
    handleSubmit();
  }, []);

  const handleSubmit = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type/getGlAccountTypeData`,
        {}
      );
      if (response.status === 200) {
        setAlldata(response.data.glAccountviewModels);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };
  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xl text-black :text-2xl  leading-normal ">
                View User
              </p>
            </div>
          </div>
        </div>
        <div className="flex justify-end items-center sm:px-6 lg:px-8 bg-blue-100  mt-1 ">
          <div className=" flex justify-center  rounded-md border border-transparent  px-5 mx-4 text-sm  font-medium text-white  hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
            <input
              type="search"
              id="search"
              className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
              placeholder="search"
            />
          </div>
        </div>
        {/* table */}

        <div className="overflow-x-auto relative shadow-md  ">
          {alldata.length > 0 && (
            <>
              <div className="table-wrp block max-h-[27rem] ">
                <table className="w-full text-sm text-left text-clack dark:text-blue-100">
                  <thead className="text-xs border-b sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
                    <th scope="col" className="py-4 px-6">
                      GL ACCOUNT Type
                    </th>
                    <th scope="col" className="py-4 px-6">
                      GL ACCOUNT DESCRIPTION
                    </th>
                    <th scope="col" className="py-4 px-6">
                      ACCOUNT NUMBER
                    </th>
                    <th scope="col" className="py-4 px-6">
                      OPENING BALANCE
                    </th>
                    <th scope="col" className="py-4 px-6">
                      CLOSING BALANCE
                    </th>
                  </thead>

                  <tbody>
                    <>
                      {alldata.map(
                        ({
                          strGLAccountType,
                          strGLAccountDescription,
                          strAccountNumber,
                          strOpeningBalance,
                          strClosingBalance,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                          >
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strGLAccountType || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strGLAccountDescription || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strAccountNumber || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strOpeningBalance || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strClosingBalance || "-"}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  </tbody>
                </table>
              </div>
            </>
          )}
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
